import socket
import binascii
import time
import sys

sys.path.insert(1, '/Users/barendmosch/source/repos/pycambot')
import CameraController

def go_left(speed):
        camera.left(speed)
        # camera.cancel

def go_right(speed):
        camera.right(speed)
        # camera.cancel

def go_up(speed):
        camera.up(speed)
        # camera.cancel

def go_down(speed):
        camera.down(speed)
        # camera.cancel

if __name__ == "__main__":
        face_x = int(sys.argv[1])
        face_y = int(sys.argv[2])
        res_x = int(sys.argv[3])
        res_y = int(sys.argv[4])

        ip = '192.168.1.188'
        port = 52381
        buffer_size = 1

        camera = CameraController.PTZOptics20x(ip, port)
        camera.init()

        width = 255
        height = 255
        margin = 10
        speed = 20

        centered_face_x = (res_x / 2) - ((width - 1) / 2)
        centered_face_y = (res_y / 2) - ((height - 1) / 2)

        if (face_x <= (centered_face_x - margin)):
                go_left(speed)
        if (face_x >= (centered_face_x + margin)):
                go_right(speed)
        if (face_y <= (centered_face_y - margin)):
                go_up(speed)
        if (face_y >= (centered_face_y + margin)):
                go_down(speed)
                
        camera.stop()
        camera.end()