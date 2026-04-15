import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Eureka } from 'eureka-js-client';

@Injectable()
export class EurekaService implements OnModuleInit, OnModuleDestroy {
  private client: Eureka;

  onModuleInit() {
    this.client = new Eureka({
      instance: {
        app: 'user-ms',              // Nom du service visible dans Eureka
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
      } else {
        console.log('User MS registered in Eureka');
      }
    });
  }

  onModuleDestroy() {
    this.client.stop();
  }
}