## Begin 

```
Begin("<context-name>")
```

> start a new context definition

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
