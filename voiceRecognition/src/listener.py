from pydub import AudioSegment
from pydub.playback import play

try:
        song = AudioSegment.from_wav("output.wav")
        play(song)
except:
        print("Exception, cannot play sound file.wav")
