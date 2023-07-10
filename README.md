## API

<docgen-index>

* [`connect(...)`](#connect)
* [`disconnect()`](#disconnect)
* [`startObserver()`](#startobserver)
* [`stopObserver()`](#stopobserver)
* [`isConnected()`](#isconnected)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### connect(...)

```typescript
connect(options: { ssid: string; password?: string; joinOnce?: boolean; isHiddenSsid?: boolean; }) => Promise<{ ssid: string | null; }>
```

| Param         | Type                                                                                          |
| ------------- | --------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ ssid: string; password?: string; joinOnce?: boolean; isHiddenSsid?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ ssid: string | null; }&gt;</code>

--------------------


### disconnect()

```typescript
disconnect() => Promise<void>
```

--------------------


### startObserver()

```typescript
startObserver() => any
```

**Returns:** <code>any</code>

--------------------


### stopObserver()

```typescript
stopObserver() => any
```

**Returns:** <code>any</code>

--------------------


### isConnected()

```typescript
isConnected() => Promise<{ isConnected: boolean | null; }>
```

**Returns:** <code>Promise&lt;{ isConnected: boolean | null; }&gt;</code>

--------------------

</docgen-api>