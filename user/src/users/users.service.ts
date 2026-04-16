import { Injectable, InternalServerErrorException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { MongoRepository} from 'typeorm';
import { User } from './user.entity';
import { NotFoundException } from '@nestjs/common';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';
import { not } from 'rxjs/internal/util/not';


@Injectable()
export class UsersService {


    constructor(
    @InjectRepository(User)
    private readonly usersRepository: MongoRepository<User>,
  ) {}


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

  // Récupérer un utilisateur par email
  async getUserByEmail(email: string): Promise<User | null> {
    return this.usersRepository.findOne({ where: { email } });
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
      
      if (data.password) {
        // Hash password with bcryptjs
        const bcrypt = require('bcryptjs');
        const salt = await bcrypt.genSalt(10);
        data.password = await bcrypt.hash(data.password, salt);
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

}


