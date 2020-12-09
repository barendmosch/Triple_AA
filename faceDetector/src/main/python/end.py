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

camera.init()

camera.stop()
camera.end()