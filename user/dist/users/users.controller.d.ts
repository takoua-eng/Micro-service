import { UsersService } from './users.service';
import { User } from './user.entity';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    getAllUsers(): Promise<User[]>;
    getUser(id: ObjectId): Promise<User | null>;
    getUsersByRole(role: string): Promise<User[]>;
    adminOnly(): string;
    getUsersByName(name: string): Promise<User[]>;
    createuser(data: Partial<CreateUserDto>): Promise<User>;
    updateuser(id: ObjectId, data: User): Promise<User | null>;
    deleteUser(id: string): Promise<void>;
    getUserById(id: string): Promise<User>;
}
