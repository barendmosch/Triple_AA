from pydub import AudioSegment
from pydub.playback import play
import scipy.signal as scs
from scipy.io import wavfile
import noisereduce as nr

FILE_NAME = "output.wav"

def listen(file_path):
        try:
                song = AudioSegment.from_wav(file_path)
                play(song)
        except:
                print("Exception, cannot play sound ", file_path)

# RAW SOUND FILE
# listen(FILE_NAME)

rate, data = wavfile.read("output.wav")
# threshhold = int(len(data))
noisy_part = data[0:len(data)]
reduced_noise = nr.reduce_noise(audio_clip=data, noise_clip=noisy_part, verbose=True)

# z, p, k = scs.besselap(1, norm='delay')


# ABLE TO RECORD AND SAVE A VOICE LINE
# NEXT STEP IS TO REMOVE THE BACKGROUND NOISE
# SEE THE SOURCES FOR THE PLAN OF APPROACH