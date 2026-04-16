"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.EurekaService = void 0;
const common_1 = require("@nestjs/common");
const eureka_js_client_1 = require("eureka-js-client");
let EurekaService = class EurekaService {
    client;
    onModuleInit() {
        this.client = new eureka_js_client_1.Eureka({
            instance: {
                app: 'user-ms',
                hostName: 'localhost',
                ipAddr: '127.0.0.1',
                statusPageUrl: 'http://localhost:3000/info',
                port: {
                    '$': 3000,
                    '@enabled': true,
                },
                vipAddress: 'user-ms',
                dataCenterInfo: {
                    '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                    name: 'MyOwn',
                },
            },
            eureka: {
                host: 'localhost',
                port: 8761,
                servicePath: '/eureka/apps/',
            },
        });
        this.client.start((error) => {
            if (error) {
                console.error('Eureka registration failed:', error);
            }
            else {
                console.log('User MS registered in Eureka');
            }
        });
    }
    onModuleDestroy() {
        this.client.stop();
    }
};
exports.EurekaService = EurekaService;
exports.EurekaService = EurekaService = __decorate([
    (0, common_1.Injectable)()
], EurekaService);
//# sourceMappingURL=eureka.service.js.map