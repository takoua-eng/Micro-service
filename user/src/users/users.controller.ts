import { Controller, Query } from '@nestjs/common';
import { UsersService } from './users.service';
import { User } from './user.entity';
import { Get, Post, Put, Delete, Body, Param } from '@nestjs/common';
import { CreateUserDto } from './CreateUser.dto';
import { ObjectId } from 'mongodb';



@Controller('users')
export class UsersController {


    constructor(private readonly usersService: UsersService) {}

  @Get()
  getAllUsers(): Promise<User[]> {
    return this.usersService.getAllUsers();
  }

@Get('getuserbyid/:id')
getUser(@Param('id') id: ObjectId) {
    return this.usersService.findonebyid(id);
}

@Get(':id')
  getUserById(@Param('id') id: string): Promise<User> {
    return this.usersService.getUserById(id);
  }

  @Get('role/:role')
  getUsersByRole(@Param('role') role: string): Promise<User[]> {
    return this.usersService.getUsersByRole(role);
  }

  @Get('name/:name')
  getUsersByName(@Param('name') name: string): Promise<User[]> {
    return this.usersService.getUsersByName(name);
  }


    @Post('add')
   createuser(@Body() data: Partial<CreateUserDto> ){
    return  this.usersService.creatuser(data);
    }


  @Put('updateuser/:id')
  updateuser(@Param('id') id:ObjectId, @Body() data: User){
    return this.usersService.updateuser(id,data)
  }

  @Delete('deleteuser/:id')
  deleteUser(@Param('id') id: string): Promise<void> {
    return this.usersService.deleteUser(id);
  }



  

  @Post('/send')
async createAndSend(@Body() body: any) {

  const user = await this.usersService.saveAndSendUser(body);

  return {
    message: '✅ User créé et envoyé à RabbitMQ',
    user,
  };
}







}
