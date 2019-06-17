## EndPackage 

```
EndPackage( )
```

> end a package definition

### Examples

``` 
>> BeginPackage("Test`")

>> $ContextPath
{Test`,System`}

>> EndPackage( )

>> $ContextPath
{Test`,System`,Global`}
```
