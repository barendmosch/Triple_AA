import socket
import binascii
import time
import sys

sys.path.insert(1, '/Users/barendmosch/source/repos/pycambot')
import CameraController

ip = '192.168.1.188'
port = 52381
buffer_size = 1

camera = CameraController.PTZOptics20x(ip, port)
print(camera._tcp_host, camera._tcp_port)

right_limit = 2412
margin = 10
go = True

camera.init()
camera.right(10)
# 10 is quite a good speed
while (go):
        i = camera.get_pan_tilt_position()
        y = camera.get_zoom_position()

        if (i[0] >= right_limit - 10 and
                 i[0] <= right_limit + 10 and 
                 i[0] != -1 and 
                 i[0] != 0):
                go = False

        print("pos: ", i[0])

camera.stop()
camera.end()