## Message

```
Message(symbol::msg, expr1, expr2, ...)
```

> displays the specified message, replacing placeholders in the message text with the corresponding expressions.

### Examples

```
>> a::b = "Hello world!"
Hello world!
    
>> Message(a::b)
a: Hello world!
    
>> a::c := "Hello `1`, Mr 00`2`!"
    
>> Message(a::c, "you", 3 + 4)
a: Hello you, Mr 007!  
```