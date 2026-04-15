import { Injectable, InternalServerErrorException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { MongoRepository} from 'typeorm';
import { User } from './user.entity';
import { NotFoundException } from '@nestjs/common';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';
import { not } from 'rxjs/internal/util/not';
import { UserDto } from './user.dto';
// eslint-disable-next-line @typescript-eslint/no-require-imports
const amqp = require('amqplib');


@Injectable()
export class UsersService {


    constructor(
    @InjectRepository(User)
    private readonly usersRepository: MongoRepository<User>,
  ) {}

  // ---------- RabbitMQ : envoi direct JSON via amqplib ----------
  private async publishToQueue(queue: string, data: object): Promise<void> {
    const connection = await amqp.connect('amqp://guest:guest@localhost:5672');
    const channel = await connection.createChannel();
    await channel.assertQueue(queue, { durable: true });
    channel.sendToQueue(
      queue,
      Buffer.from(JSON.stringify(data)),
      { contentType: 'application/json', persistent: true },
    );
    await channel.close();
    await connection.close();
  }


  // Récupérer tous les utilisateurs
  async getAllUsers(): Promise<User[]> {
    return this.usersRepository.find();
  }

// Récupérer un utilisateur par ID
 /* async getUserById(id: string): Promise<User> {
    const user = await this.usersRepository.findOneBy({ id });
    if (!user) {
      throw new NotFoundException(`User with id ${id} not found`);
    }
    return user;
  }*/

 async findonebyid(id: ObjectId): Promise<User | null> {
  try {
    const user = await this.usersRepository.findOneBy(id);

    if (!user) {
      throw new NotFoundException(`Utilisateur avec ID ${id} introuvable`);
    }

    return user;
  } catch (error) {
    throw new InternalServerErrorException(error.message);
  }
}


// Récupérer un utilisateur par ID
  async getUserById(id: string): Promise<User> {
  const user = await this.usersRepository.findOne({
    where: {
      _id: new ObjectId(id),
    },
  });

  if (!user) {
    throw new NotFoundException(
      `User with id ${id} not found`,
    );
  }

  return user;

  }



  // Récupérer les utilisateurs par rôle
  async getUsersByRole(role: string): Promise<User[]> {
    return this.usersRepository.find({ where: { role } });
  }

  // Récupérer les utilisateurs par nom
  async getUsersByName(name: string): Promise<User[]> {
    return this.usersRepository.find({ where: { name } });
  }

  /* Créer un nouvel utilisateur
  async createUser(data: Partial<User>): Promise<User> {
    const user = this.usersRepository.create(data);
    return this.usersRepository.save(user);
  }*/

    async creatuser(data: Partial<CreateUserDto>) {
    try {
      if (!data){
        throw new NotFoundException(`Invalid user data`);
      }
      const newuser = await this.usersRepository.create(data);
      return await this.usersRepository.save(newuser);
    } catch (error) {
      console.log('erreur'+error.message)
      throw new InternalServerErrorException(
        `Failed to create user: ${error.message}`);
    }
  }




// Mettre à jour un utilisateur
  async updateuser(id: ObjectId, data: User): Promise<User | null> {
    try {
     const res =  await this.usersRepository.update(id, data);
     if (res.affected==0){
      throw new NotFoundException()
     }
      return await this.usersRepository.findOneBy(id);
    } catch (error) {
     throw new InternalServerErrorException(error.message);
    }
  }

// Supprimer un utilisateur
  async deleteUser(id: string): Promise<void> {
    await this.usersRepository.delete(id);
  }





//*************************pour rabbit mq***************************

  sendUserEvent(user: any): void {
    const userDto: UserDto = {
      id: user._id?.toString(),
      name: user.name,
      email: user.email,
      role: user.role,
    };
    // fire-and-forget (pas de await) — erreurs loggées
    this.publishToQueue('userRestaurantQueue', userDto)
      .then(() => console.log('✅ User envoyé vers RabbitMQ :', userDto))
      .catch((error) => console.error('❌ Erreur envoi RabbitMQ', error));
  }

  async saveAndSendUser(data: Partial<CreateUserDto>) {
    // 1️⃣ Sauvegarde en base
    const newUser = this.usersRepository.create(data);
    const savedUser = await this.usersRepository.save(newUser);
    console.log('✅ User sauvegardé en DB :', savedUser);

    // 2️⃣ Construire DTO
    const userDto: UserDto = {
      id: savedUser.id?.toString(),
      name: savedUser.name,
      email: savedUser.email,
      role: savedUser.role,
    };

    // 3️⃣ Envoi direct JSON brut dans la queue (compatible Spring AMQP)
    await this.publishToQueue('userRestaurantQueue', userDto);
    console.log('📤 User envoyé vers RabbitMQ :', userDto);

    return savedUser;
  }





}


