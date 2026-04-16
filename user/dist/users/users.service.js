"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.UsersService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const user_entity_1 = require("./user.entity");
const common_2 = require("@nestjs/common");
const mongodb_1 = require("mongodb");
let UsersService = class UsersService {
    usersRepository;
    constructor(usersRepository) {
        this.usersRepository = usersRepository;
    }
    async getAllUsers() {
        return this.usersRepository.find();
    }
    async findonebyid(id) {
        try {
            const user = await this.usersRepository.findOneBy(id);
            if (!user) {
                throw new common_2.NotFoundException(`Utilisateur avec ID ${id} introuvable`);
            }
            return user;
        }
        catch (error) {
            throw new common_1.InternalServerErrorException(error.message);
        }
    }
    async getUserById(id) {
        const user = await this.usersRepository.findOne({
            where: {
                _id: new mongodb_1.ObjectId(id),
            },
        });
        if (!user) {
            throw new common_2.NotFoundException(`User with id ${id} not found`);
        }
        return user;
    }
    async getUsersByRole(role) {
        return this.usersRepository.find({ where: { role } });
    }
    async getUsersByName(name) {
        return this.usersRepository.find({ where: { name } });
    }
    async getUserByEmail(email) {
        return this.usersRepository.findOne({ where: { email } });
    }
    async creatuser(data) {
        try {
            if (!data) {
                throw new common_2.NotFoundException(`Invalid user data`);
            }
            if (data.password) {
                const bcrypt = require('bcryptjs');
                const salt = await bcrypt.genSalt(10);
                data.password = await bcrypt.hash(data.password, salt);
            }
            const newuser = await this.usersRepository.create(data);
            return await this.usersRepository.save(newuser);
        }
        catch (error) {
            console.log('erreur' + error.message);
            throw new common_1.InternalServerErrorException(`Failed to create user: ${error.message}`);
        }
    }
    async updateuser(id, data) {
        try {
            const res = await this.usersRepository.update(id, data);
            if (res.affected == 0) {
                throw new common_2.NotFoundException();
            }
            return await this.usersRepository.findOneBy(id);
        }
        catch (error) {
            throw new common_1.InternalServerErrorException(error.message);
        }
    }
    async deleteUser(id) {
        await this.usersRepository.delete(id);
    }
};
exports.UsersService = UsersService;
exports.UsersService = UsersService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(user_entity_1.User)),
    __metadata("design:paramtypes", [typeorm_2.MongoRepository])
], UsersService);
//# sourceMappingURL=users.service.js.map