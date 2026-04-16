import { Strategy } from 'passport-jwt';
declare const KeycloakStrategy_base: new (...args: [opt: import("passport-jwt").StrategyOptionsWithRequest] | [opt: import("passport-jwt").StrategyOptionsWithoutRequest]) => Strategy & {
    validate(...args: any[]): unknown;
};
export declare class KeycloakStrategy extends KeycloakStrategy_base {
    constructor();
    validate(payload: any): Promise<any>;
}
export {};
