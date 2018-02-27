## Block

``` 
Block({list_of_local_variables}, expr )
``` 

> evaluates `expr` for the `list_of_local_variables`

### Examples
``` 
>> $blck=Block({$i=10}, $i=$i+1; Return($i))
11
``` 
