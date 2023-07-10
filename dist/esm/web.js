import { WebPlugin } from '@capacitor/core';
export class WifiWeb extends WebPlugin {
    async connect(options) {
        console.log(options);
        return { ssid: null };
    }
    async disconnect() {
        return;
    }
    async startObserver() {
        return;
    }
    async stopObserver() {
        return;
    }
    async isConnected() {
        // console.log(options);
        return { isConnected: null };
    }
}
//# sourceMappingURL=web.js.map