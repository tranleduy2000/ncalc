## Clear 

```
Clear(symbol1, symbol2,...)
```
> clears all values of the given symbols.

`Clear` does not remove attributes, options, and default values associated with the symbols. Use `ClearAll` to do so.
  
### Examples

``` 
>> a=2
2

>> Definition(a)
{a=2}
 
>> Clear(a)
>> a
a
```
