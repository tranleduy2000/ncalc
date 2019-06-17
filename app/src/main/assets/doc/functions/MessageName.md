## MessageName

```
MessageName(symbol, msg)
```

or

```
symbol::msg
```

> `symbol::msg` identifies a message. `MessageName` is the head of message IDs of the form `symbol::tag`.

### Examples

 
The second parameter 'tag' is interpreted as a string.

```
>> FullForm(a::b)
MessageName(a, b)

>> FullForm(a::"b")
MessageName(a, "b")
```