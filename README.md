# Indented Line Configuration Format (ILCF)
A cascading configuration file format based upon tabulation.

The keys prior to a line are prefixed to the line's key with a period as a delimiter dependant upon tabulation.

A way to create an ArrayList<String> and HashMap<String, String> is intended to be added in the future.

## Example
Configuration File
```
teams
	1
		name = Development Group \#1
		supervisor = 1 # Jacob Trott
		members
			1
				name = John Doe
				role = Lead Developer
			2
				name = Don Mason
				role = Programmer
	2
		name = Development Group \#2
		supervisor = 2 # Brian Hills
		members
			1
				name = Thomas Frank
				role = Programming Lead
			2
				name = Daniel Jones
				role = Java Developer
supervisors
	1
		name = Jacob Trott
		department = Programming
	2
		name = Brian Hills
		department = Programming
```
Java Code
```java
ILCFReader reader = new ILCFReader("/path/to/file.ilcf");
reader.read();
System.out.println(reader.getString("teams.1.name"));
System.out.println(reader.getString("teams.1.supervisor"));
System.out.println(reader.getString("supervisors.1.name"));
System.out.println(reader.getString("supervisors.1.department"));
```
Prints to Standard Output
```
Development Group #1
1
Jacob Trott
Programming
```
