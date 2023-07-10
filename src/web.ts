import { WebPlugin } from '@capacitor/core';
import { WifiPlugin } from './definitions';

export class WifiWeb extends WebPlugin implements WifiPlugin {
  async connect(options: { ssid: string, password?: string, authType?: string }): Promise<{ ssid: string | null }> {
    console.log(options);
    return { ssid: null };
  }
  async disconnect(): Promise<void> {
    return;
  }
  async startObserver(): Promise<void> {
    return;
  }
  async stopObserver(): Promise<void> {
    return;
  }
  async isConnected(): Promise<{ isConnected: boolean | null }> {
    // console.log(options);
    return { isConnected: null };
  }

}