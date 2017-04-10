GRAPHICS_WIDTH = 1600
GRAPHICS_HEIGHT = 900

Global GLOBAL_FRAME = 0

Graphics GRAPHICS_WIDTH,GRAPHICS_HEIGHT

Global G_SPEED# = 1.0 ; number of cycles per second, must be > 0.0
Global G_UPDATE_TIME = 60 / G_SPEED# ; time taken for one complete cycle
Global G_PREV_TIME = 0 ; records the last time the gradient was at phase 0.0

; rgb values of first color in gradient

Global G_R1 = 255
Global G_G1 = 0
Global G_B1 = 0

; rgb values of second color in gradient

Global G_R2 = 255
Global G_G2 = 216
Global G_B2 = 0

; differences in rgb values of the two colors (used to calculate intermediate colors)

Global G_RDIFF = G_R2 - G_R1
Global G_GDIFF = G_G2 - G_G1
Global G_BDIFF = G_B2 - G_B1

AutoMidHandle True
FONT = LoadFont("PDMS_Saleem_QuranFont-signed.ttf",54,False)
SetFont FONT

IMG_BLUE_MOSQUE = LoadImage("BlueMosque.png")

Dim IMAGES(6)

Dim LINES$(6)
LINES$(0) = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ"
LINES$(1) = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
LINES$(2) = "الرَّحْمَنِ الرَّحِيمِ"
LINES$(3) = "مَالِكِ يَوْمِ الدِّينِ"
LINES$(4) = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ"
LINES$(5) = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ"
LINES$(6) = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"

For i = 0 To 6
    Cls
    SetScale 1.5625,1.5625
    DrawImage IMG_BLUE_MOSQUE,800,450
    SetScale 1.0,1.0
    Color 255,0,255
    Text GRAPHICS_WIDTH * 0.5,120,LINES$(i),True,True
    Flip
    ScreenShot "BlueMosqueVerse" + i + ".bmp"
Next

AutoMidHandle False

For i = 0 To 6
    IMAGES(i) = LoadImage("BlueMosqueVerse" + i + ".bmp")
    MaskImage(IMAGES(i),255,0,255)
Next

For i = 0 To 6
    For j = 0 To 60
      Cls
      Color 255,0,0
      Gradient GRAPHICS_WIDTH * 0.5,120,StringWidth(LINES$(i)),StringHeight(LINES$(i)), True, True
      DrawImage(IMAGES(i),0,0)
      GLOBAL_FRAME = GLOBAL_FRAME + 1
      Flip
      ScreenShot("./TimeLapse/" + GLOBAL_FRAME + ".bmp")
      Next
Next

Function Gradient(x,y,w,h,cx = False,cy = False)
	Local i,j
	Local Offset
	Local Length
	Local HalfLength#
	Local Time
	Local Elapsed
	Local Phase#,Factor#

	Time = GLOBAL_FRAME
	Elapsed = Time - G_PREV_TIME

	Phase# = (Float(Elapsed Mod G_UPDATE_TIME) / G_UPDATE_TIME)
	
	If Elapsed >= G_UPDATE_TIME
		G_PREV_TIME = Time - (Elapsed Mod G_UPDATE_TIME)
	EndIf

	Length = w

	If cx = True
		x = x - w / 2
	EndIf
	
	If cy = True
		y = y - h / 2
	EndIf


	HalfLength# = Float(Length) / 2

	For i = 0 To Length - 1
	
		j = (i + (Length * Phase#)) Mod Length

		If Float(j) / Length >= 0.5
			Factor# = 1.0 - (j - HalfLength#) / HalfLength#		
		Else
			Factor# = j / HalfLength#
		EndIf

		Color G_R1 + Factor# * G_RDIFF,G_G1 + Factor# * G_GDIFF,G_B1 + Factor# * G_BDIFF

		Rect x + Offset,y,1,h,True
		Offset = Offset + 1
	Next
	
	Return 1
End Function