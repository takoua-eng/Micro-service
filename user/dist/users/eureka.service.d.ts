import { OnModuleInit, OnModuleDestroy } from '@nestjs/common';
export declare class EurekaService implements OnModuleInit, OnModuleDestroy {
    private client;
    onModuleInit(): void;
    onModuleDestroy(): void;
}
