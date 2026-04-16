import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UsersModule } from './users/users.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from './users/user.entity';
import { EurekaService } from './users/eureka.service';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'mongodb',
      host: 'localhost',
      port: 27017,
      database: 'locationA',
      entities: [User],
      synchronize: true,
    }),
    UsersModule],
  controllers: [AppController],
  providers: [AppService, EurekaService],
})
export class AppModule {}
