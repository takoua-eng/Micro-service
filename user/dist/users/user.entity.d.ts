import { ObjectId } from 'mongodb';
export declare class User {
    id: ObjectId;
    name: string;
    email: string;
    role: string;
    password?: string;
}
