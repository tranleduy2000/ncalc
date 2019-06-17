## End 

```
End( )
```

> end a context definition started with `Begin`

### Examples

``` 
>> Begin("mytest`") 

>> Context()
mytest`

>> $ContextPath
{System`,Global`} 

>> End()
mytest`

>> Context()
Global`

>> $ContextPath
{System`,Global`}

```
