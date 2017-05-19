## MachineNumberQ
```
MachineNumberQ(expr)
```
> returns `True` if `expr` is a machine-precision real or complex number.

### Examples
```
>> MachineNumberQ(3.14159265358979324)
False
 
>> MachineNumberQ(1.5 + 2.3*I)
True
 
>> MachineNumberQ(2.71828182845904524 + 3.14159265358979324*I)
False
 
>> MachineNumberQ(1.5 + 3.14159265358979324*I)    
True

>> MachineNumberQ(1.5 + 5 *I)
True    
```