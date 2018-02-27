## ElementData

```
ElementData("name“, ”property")
```
> gives the value of the property for the chemical specified by name.

```
ElementData(n, "property")
```

> gives the value of the property for the nth chemical element.

`ElementData` uses data from [Wikipedia - List of data references for chemical elements](https://en.wikipedia.org/wiki/List_of_data_references_for_chemical_elements)

### Examples

```
>> ElementData(74)
"Tungsten"

>> ElementData("He", "AbsoluteBoilingPoint")
4.22

>> ElementData("Carbon", "IonizationEnergies")
{1086.5,2352.6,4620.5,6222.7,37831,47277.0}

>> ElementData(16, "ElectronConfigurationString")
"[Ne] 3s2 3p4"

>> ElementData(73, "ElectronConfiguration")
{{2},{2,6},{2,6,10},{2,6,10,14},{2,6,3},{2}}
```

Some properties are not appropriate for certain elements:
```
>> ElementData("He", "ElectroNegativity")
Missing(NotApplicable)
```

Some data is missing:
```
>> ElementData("Tc", "SpecificHeat")
Missing(NotAvailable)
```

All the known properties:
```
>> ElementData("Properties")
{"Abbreviation","AbsoluteBoilingPoint","AbsoluteMeltingPoint","AtomicNumber","AtomicRadius","AtomicWeight","Block",
"BoilingPoint","BrinellHardness","BulkModulus","CovalentRadius","CrustAbundance","Density","DiscoveryYear","ElectroNegativity",
"ElectronAffinity","ElectronConfiguration","ElectronConfigurationString","ElectronShellConfiguration","FusionHeat",
"Group","IonizationEnergies","LiquidDensity","MeltingPoint","MohsHardness","Name","Period","PoissonRatio","Series",
"ShearModulus","SpecificHeat","StandardName","ThermalConductivity","VanDerWaalsRadius","VaporizationHeat","VickersHardness",
"YoungModulus"}
```