import { Entity, ObjectIdColumn, Column } from 'typeorm';
import { ObjectId } from 'mongodb'; 


@Entity()
export class User {
  @ObjectIdColumn()
  id: ObjectId;

  @Column()
  name: string;

  @Column()
  email: string;

  @Column()
  role: string;
}