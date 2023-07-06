## API

<docgen-index>

* [`connect(...)`](#connect)
* [`disconnect()`](#disconnect)

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

</docgen-api>