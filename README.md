
# jetgeo

A library for off-line inverse geocoding that supports location up to district level

<!-- PROJECT SHIELDS -->

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/linG5821/jetgeo/jetgeo)

![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/linG5821/jetgeo?sort=semver)

![GitHub forks](https://img.shields.io/github/forks/linG5821/jetgeo)

![GitHub](https://img.shields.io/github/license/linG5821/jetgeo)

<!-- PROJECT LOGO -->
<br />




## 目录



- [快速开始](#快速开始)
- [文件目录说明](#文件目录说明)
- [使用到的框架](#使用到的框架)
- [贡献者](#贡献者)
    - [如何参与开源项目](#如何参与开源项目)
- [作者](#作者)
- [鸣谢](#鸣谢)

### 快速开始

1. 导入依赖

   ```xml
   <dependency>
       <groupId>com.ling5821</groupId>
       <artifactId>jetgeo</artifactId>
       <version>${jetgeo.version}</version>
   </dependency>
   ```

2. 下载项目目录地理位置资源包 [geodata.7z](https://github.com/linG5821/jetgeo/blob/main/data/geodata.7z)

3. 解压数据到你的目录 例如 `/data/geodata`

   > 注意: 解压后目录结构
   >
   > geodata
   > ├── province
   > ├── city
   > ├── district

4. 一个例子

   ```java
   public class JetGeoExample {
       public static final JetGeo jetGeo;
       static {
           JetGeoProperties properties = new JetGeoProperties();
           //填写你的geodata的目录
           properties.setGeoDataParentPath("/data/geodata");
           //设置逆地理编码的级别
           //properties.setLevel(LevelEnum.province);
           //properties.setLevel(LevelEnum.city);
           properties.setLevel(LevelEnum.district);
           jetGeo = new JetGeo(properties);
       }
   
       public static void main(String[] args) {
           GeoInfo geoInfo = jetGeo.getGeoInfo(32.053197915979325,118.85999259252777);
           System.out.println(geoInfo);
       }
   }
   ```

   ```shell
   GeoInfo(formatAddress=江苏省南京市玄武区, province=江苏省, city=南京市, district=玄武区, street=, adcode=320102, level=null)
   ```





### 使用到的框架

- [google-s2](git@github.com:google/s2-geometry-library-java.git)

### 贡献者

请阅读**CONTRIBUTING.md** 查阅为该项目做出贡献的开发者。

#### 如何参与开源项目

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。


1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



### 版本控制

该项目使用Git进行版本管理。您可以在repository参看当前可用版本。

### 作者

<table>
  <tbody>
    <tr>
        <td align="center" valign="top">
            <img width="125" height="125" src="https://github.com/linG5821.png?s=150">
            <br>
            <strong>linG5821</strong>
            <br>
            <a href="https://github.com/linG5821">@linG5821</a>
        </td>
     </tr>
  </tbody>
</table>

*您也可以在贡献者名单中参看所有参与该项目的开发者。*

### 版权说明

Apache License 2.0, see [LICENSE](https://github.com/linG5821/jetgeo/blob/master/LICENSE).

### 鸣谢
