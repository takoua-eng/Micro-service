import { Controller, UseGuards } from '@nestjs/common';
import { UsersService } from './users.service';
import { User } from './user.entity';
import { Get, Post, Put, Delete, Body, Param } from '@nestjs/common';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';
import { AuthGuard } from '@nestjs/passport';
import { Roles } from '../auth/roles.decorator';
import { RolesGuard } from '../auth/roles.guard';

@Controller('users')
@UseGuards(AuthGuard('jwt')) // protège tout le controller
export class UsersController {

  constructor(private readonly usersService: UsersService) { }

  // 🔹 Accessible USER + ADMIN
  @Get()
  getAllUsers(): Promise<User[]> {
    return this.usersService.getAllUsers();
  }

  // 🔹 Accessible USER + ADMIN
  @Get('getuserbyid/:id')
  getUser(@Param('id') id: ObjectId) {
    return this.usersService.findonebyid(id);
  }



  // 🔹 Accessible ADMIN seulement
  @Get('role/:role')
  @UseGuards(RolesGuard)
  @Roles('admin')
  getUsersByRole(@Param('role') role: string): Promise<User[]> {
    return this.usersService.getUsersByRole(role);
  }


  @Get('admin')
  @UseGuards(RolesGuard)
  @Roles('admin')
  adminOnly() {
    return "Accessible uniquement ADMIN";
  }

  // 🔹 Accessible USER + ADMIN
  @Get('name/:name')
  getUsersByName(@Param('name') name: string): Promise<User[]> {
    return this.usersService.getUsersByName(name);
  }

  // 🔹 Accessible ADMIN seulement
  @Post('add')
  @UseGuards(RolesGuard)
  @Roles('admin')
  createuser(@Body() data: Partial<CreateUserDto>) {
    return this.usersService.creatuser(data);
  }

  // 🔹 Accessible ADMIN seulement
  @Put('updateuser/:id')
  @UseGuards(RolesGuard)
  @Roles('admin')
  updateuser(@Param('id') id: ObjectId, @Body() data: User) {
    return this.usersService.updateuser(id, data);
  }

  // 🔹 Accessible ADMIN seulement
  @Delete('deleteuser/:id')
  @UseGuards(RolesGuard)
  @Roles('admin')
  deleteUser(@Param('id') id: string): Promise<void> {
    return this.usersService.deleteUser(id);
  }

    // 🔹 Accessible USER + ADMIN
  @Get(':id')
  getUserById(@Param('id') id: string): Promise<User> {
    return this.usersService.getUserById(id);
  }


}