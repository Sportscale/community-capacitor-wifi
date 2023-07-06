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

}