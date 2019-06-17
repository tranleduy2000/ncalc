## BeginPackage 

```
BeginPackage("<context-name>")
```

> start a new package definition

### Examples

``` 
>> BeginPackage("Test`")

>> $ContextPath
{Test`,System`}

>> EndPackage( )

>> $ContextPath
{Test`,System`,Global`}
```
