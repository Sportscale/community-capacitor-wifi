import { WebPlugin } from '@capacitor/core';
import { WifiPlugin } from './definitions';
export declare class WifiWeb extends WebPlugin implements WifiPlugin {
    connect(options: {
        ssid: string;
        password?: string;
        authType?: string;
    }): Promise<{
        ssid: string | null;
    }>;
    disconnect(): Promise<void>;
}
