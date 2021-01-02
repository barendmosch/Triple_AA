import socket
import binascii
import time
import sys

sys.path.insert(1, '/Users/barendmosch/source/repos/pycambot')
import CameraController

def zoom_in(w, h):
        # camera.zoomin(1)
        camera.left(10)
        camera.zoomstop()
        camera.cancel()

def zoom_out(w, h):
        # camera.zoomout(1)
        camera.right(10)
        camera.zoomstop()
        camera.cancel()

if __name__ == "__main__":
        width = int(sys.argv[1])
        height = int(sys.argv[2])
        bound_width = 255
        bound_height = 255

        ip = '192.168.1.188'
        port = 52381
        buffer_size = 1

        camera = CameraController.PTZOptics20x(ip, port)
        camera.init()

        if (width != bound_width and height != bound_height):
                if (width < bound_width and height < bound_height):
                        zoom_in(width, height)
                elif (width > bound_width and height > bound_height):
                        zoom_out(width, height)

        camera.stop()
        camera.end()
