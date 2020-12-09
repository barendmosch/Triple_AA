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

left_limit = 63225
right_limit = 2412
margin = 10
go = True
rotation = True

camera.init()

# Go left initially then rotate right to left every time the camera can't go furtherma
camera.left(10)
# 10 is quite a good speed
while (go):
        # if (rotation == True):
        #         camera.left(10)
        # else:
        #         camera.right(10)

        i = camera.get_pan_tilt_position()

        if(i[0] > 0):
                if (i[0] >= left_limit - margin and i[0] <= left_limit + margin):
                        camera.stop()
                        camera.right(10)
                if (i[0] >= right_limit - margin and i[0] <= right_limit + margin):
                        camera.stop()
                        camera.left(10)

        print("pos: ", i[0])
        y = camera.get_zoom_position()

camera.stop()
camera.end()