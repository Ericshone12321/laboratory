from PIL import Image
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans

def Img_to_RGBdata(image_path):
    with Image.open(image_path) as img:
        img = img.convert("RGB")
        width, height = img.size
        rgb_list = []

        for y in range(height):
            list = []
            for x in range(width):
                r, g, b = img.getpixel((x, y))
                list.append([r, g, b])
            rgb_list.append(list)
    return rgb_list


def rgbData_to_image(data, output_path):
    img = Image.new("RGB", (len(data[0]), len(data)))
    rgb_list = []
    for i in data:
        for j in i:
            rgb_list.append(tuple(j)) 
    img.putdata(rgb_list)
    img.save(output_path)


def toGray(data):
    grayData = []
    for i in range(len(data)):
        list = []
        for j in range(len(data[0])):
            grayScale = int(data[i][j][0] * 0.2126 + 0.7152 * data[i][j][1] + 0.0722 * data[i][j][2])
            list.append([grayScale, grayScale, grayScale])
        grayData.append(list)
    return grayData


path = "Binarization/fold1_0.png"
RGBdata = Img_to_RGBdata(path)
grayData = toGray(RGBdata)
grayData = np.array(grayData)
grayData = grayData[:, :, 0]
newGraydata = np.array(grayData).reshape(-1, 1)

kmeans = KMeans(n_clusters=5, random_state=0)
kmeans.fit(newGraydata)
print(kmeans.cluster_centers_)
labels = kmeans.labels_
labels = labels.reshape(grayData.shape)

clustered_image_array = kmeans.cluster_centers_[labels].astype(np.uint8).reshape(grayData.shape)
result_image = Image.fromarray(clustered_image_array)
result_image.save('clustered_image.jpg')

plt.figure(figsize=(12, 6))
plt.subplot(1, 2, 1)
plt.title('Original Image')
plt.imshow(grayData, cmap='gray')
plt.axis('off')
plt.subplot(1, 2, 2)
plt.title('K-means Clustered Image')
plt.imshow(result_image, cmap='gray')
plt.axis('off')
plt.show()