import { WebPlugin } from '@capacitor/core';
export class WifiWeb extends WebPlugin {
    async connect(options) {
        console.log(options);
        return { ssid: null };
    }
    async disconnect() {
        return;
    }
}
//# sourceMappingURL=web.js.map