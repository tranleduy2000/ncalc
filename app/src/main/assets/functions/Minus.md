## Minus

```  
Minus(expr)

-expr
```   
> is the negation of `expr`. 
 

### Examples

```   
>> -a //FullForm
"Times(-1, a)"
```
 
`Minus` automatically distributes:
```
>> -(x - 2/3)
2/3-x
```

`Minus` threads over lists:
```
>> -Range(10)
{-1,-2,-3,-4,-5,-6,-7,-8,-9,-10}
```
	
	