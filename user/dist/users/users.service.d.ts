import { MongoRepository } from 'typeorm';
import { User } from './user.entity';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';
export declare class UsersService {
    private readonly usersRepository;
    constructor(usersRepository: MongoRepository<User>);
    getAllUsers(): Promise<User[]>;
    findonebyid(id: ObjectId): Promise<User | null>;
    getUserById(id: string): Promise<User>;
    getUsersByRole(role: string): Promise<User[]>;
    getUsersByName(name: string): Promise<User[]>;
    getUserByEmail(email: string): Promise<User | null>;
    creatuser(data: Partial<CreateUserDto>): Promise<User>;
    updateuser(id: ObjectId, data: User): Promise<User | null>;
    deleteUser(id: string): Promise<void>;
}
