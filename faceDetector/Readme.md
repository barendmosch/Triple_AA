# Used sources

**Maven documentation**

- https://metamug.com/article/java/build-run-java-maven-project-command-line.html

**Jero MQ Library**

- https://github.com/zeromq/jeromq/blob/master/README.md
- https://github.com/booksbyus/zguide

**ZeroMQ Tutorials and documentation**

- https://www.youtube.com/watch?v=UrwtQfSbrOs&t=1111s
- https://www.zeromq.ord/socket-api/
- http://zguide.zeromq.org/page:chapter2

**Might be a nice face detection tutorial:**
- https://www.baeldung.com/java-opencv (14-10-2020)

**Different face recognition algorithms:**
- https://facedetection.com/algorithms/
  - 3 types:
    - Model-based Face Tracking
    - Weak classifier cascades (Haar) (Most commonly used)
    - HOGs and Deep Learning

**Haar like Feature for face detection:**
- https://medium.com/swlh/haar-cascade-classifiers-in-opencv-explained-visually-f608086fc42c (15-10-2020)
- http://www.willberger.org/cascade-haar-explained/ ((15-10-2020))

# Quick definitions:

## Face detection 

The Viola Jones Face Detection Algorithm has four stages:
- Haar Feature Selection
- Creating  Integral Images
- Adaboost Training
- Cascading Classifiers

### Haar like features
- Haar Cascade is a machine learning object detection algorith used to identify objects in an image or video and based on the concept of ​​ features proposed by Paul Viola and Michael Jones in their paper "Rapid Object Detection using a Boosted Cascade of Simple Features" in 2001.
- A method to detect features of the face and therefore the face itself by calculating the difference in brightness of sections of the face. This method requires an image as imput, converts the image to a black-white image. And then determines the edges and lines on the section of the image to look for a clear difference in brightness. The difference is calculated by taking the average of the white section and substracting it from the average of the black section. The result is called a feature value. If the feature value is close to 255, the more likely whatever the Haar-like feature represents exists in this section. This way we can identify the location of the eyes (because the eyes are usually a bit darker than the skin around it), the nose (nose is usually more brighter than the skin around it) and mouth. And with the features identified, we can draw a lines around the features to create a section where the face probably is. 

Initially, the algorithm needs a lot of positive images of faces and negative images

Substracting the white section from the black section is gonna taking a lot of time, thats why the Integral Image aproach might be a better way to tackle things:
- Original Image: O(n^2)
- Integral Image: O(1) (constant)

**Source:**
- https://youtube.com/watch?v=x41KFOFGnUE
