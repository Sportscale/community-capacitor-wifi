import { WebPlugin } from '@capacitor/core';
export class WifiWeb extends WebPlugin {
    async getIP() {
        return { ip: null };
    }
    async getSSID() {
        return { ssid: null };
    }
    async connect(options) {
        console.log(options);
        return { ssid: null };
    }
    async connectPrefix(options) {
        console.log(options);
        return { ssid: null };
    }
    async disconnect(options) {
        console.log(options);
        return;
    }
    async newConnect(options) {
        console.log(options);
        return { ssid: null };
    }
}
//# sourceMappingURL=web.js.map