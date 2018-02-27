## Strings

Strings can be entered with `"` as delimeters:
```
>> "Hello world!"
"Hello world!"
```

Strings can be joined using the `<>` operator:
```
>> "Hello" <> " " <> "world!"
"Hello world!"
```

Numbers cannot be joined to strings:
```
>> "Debian" <> 6
"Debian" <> 6
```

They have to be converted to strings using `ToString` first:
```
>> "Debian" <> ToString(6)
"Debian6"
```