var capacitorWifi = (function (exports, core) {
    'use strict';

    const Wifi = core.registerPlugin('Wifi', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.WifiWeb()),
        electron: () => window.CapacitorCustomPlatform.plugins.Wifi,
    });

    class WifiWeb extends core.WebPlugin {
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

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        WifiWeb: WifiWeb
    });

    exports.Wifi = Wifi;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
