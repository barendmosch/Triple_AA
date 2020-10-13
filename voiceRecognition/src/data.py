from scipy.io import wavfile
from pydub import AudioSegment 

samplerate, data = wavfile.read('Note.wav')
wav_file = AudioSegment.from_file(file="Note.wav", format="wav")
wav_file2 = AudioSegment.from_file(file="output.wav", format="wav")

print(type(wav_file))  
# OUTPUT: <class 'pydub.audio_segment.AudioSegment'> 
  
#  To find frame rate of song/file 
print(wav_file.frame_rate)    
# OUTPUT: 22050  
  
# To know about channels of file 
print(wav_file.channels)  
# OUTPUT: 1 
  
# Find the number of bytes per sample  
print(wav_file.sample_width )  
# OUTPUT : 2 
  
# Find Maximum amplitude  
print(wav_file.max) 
# OUTPUT 17106 
  
# To know length of audio file 
print(len(wav_file)) 
