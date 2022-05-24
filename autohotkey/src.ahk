~Pause::Pause
~Insert::Suspend
#IfWinActive ahk_exe DNF.exe
SetStoreCapslockMode, off

$a::
Loop{
	If Not GetKeyState("a", "P")
		Break
	Send {a Down}
	Sleep 20
	Send {a Up}
	Sleep 20
}
Return ;

$x::
Loop{
	If Not GetKeyState("x", "P")
		Break
	Send {x Down}
	Sleep 20
	Send {x Up}
	Sleep 20
}
Return ;
