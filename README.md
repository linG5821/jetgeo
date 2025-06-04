<img align="center" src="./images/logo.png" width="1134" alt="jetgeo Logo" />

<p align="center">
   <a href="https://github.com/linG5821/jetgeo/blob/main/.github/workflows/maven-publish.yml"><img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/linG5821/jetgeo/jetgeo"></a>
   <a href="https://github.com/linG5821/jetgeo/releases"><img alt="GitHub release (latest SemVer)" src="https://img.shields.io/github/v/release/linG5821/jetgeo?sort=semver"></a>
   <img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/linG5821/jetgeo">
   <a href="https://github.com/linG5821/jetgeo/blob/main/LICENSE"><img alt="GitHub" src="https://img.shields.io/github/license/linG5821/jetgeo"></a>
</p>

<p>
   jetgeo 是一个用于离线逆地理编码的库，支持转换位置到县/地区级别, 
通过它你可以消耗一定的内存以换取一个内存级别的转换。主要适用于精度要求不高的一些服务端场景。
如果你需要一个精确定位的场景,那么它可能并不合适,此时你可能需要适用地理位置信息提供商的一些服务, 
但他们往往有次数限制或者需要支付更多的费用。
</p>

## 目录

- [快速开始](#快速开始)
- [核心依赖](#核心依赖)
- [贡献者](#贡献者)
- [如何参与开源项目](#如何参与开源项目)
- [作者](#作者)
- [鸣谢](#鸣谢)

### 快速开始

1. 导入依赖

   ```xml
   <dependency>
       <groupId>com.ling5821</groupId>
       <artifactId>jetgeo-core</artifactId>
       <version>${jetgeo.version}</version>
   </dependency>
   ```

2. 下载项目目录地理位置资源包 [geodata.7z](https://github.com/linG5821/jetgeo/blob/main/data/geodata.7z)

3. 解压数据到你的目录 例如 `/data/geodata`

> 注意: 解压后目录结构
>
> geodata
>
> ├── province
>
> ├── city
>
> ├── district

5. 一个例子

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
           //这里的经纬度坐标需要使用 wgs84 坐标系
           GeoInfo geoInfo = jetGeo.getGeoInfo(32.053197915979325,118.85999259252777);
           System.out.println(geoInfo);
       }
   }
   ```

   ```shell
   GeoInfo(formatAddress=江苏省南京市玄武区, province=江苏省, city=南京市, district=玄武区, street=, adcode=320102, level=null)
   ```

### 核心依赖

- [google-s2](https://github.com/google/s2-geometry-library-java.git)
- [hutool](https://github.com/dromara/hutool.git)
- [lombok](https://github.com/projectlombok/lombok.git)
- [spring-boot](https://github.com/spring-projects/spring-boot.git)
- [junit5](https://github.com/junit-team/junit5.git)

### 贡献者

请阅读**CONTRIBUTING.md** 查阅为该项目做出贡献的开发者。

### 如何参与开源项目

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

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

*您也可以在贡献者名单中参看所有[参与该项目的开发者](https://github.com/linG5821/jetgeo/graphs/contributors).*

### 版权说明

Apache License 2.0, see [LICENSE](https://github.com/linG5821/jetgeo/blob/master/LICENSE).

### 鸣谢

<table>
  <tbody>
  <tr>
    <td align="center" valign="top">
      <img width="125" height="125" src="https://github.com/Tangchaoyue.png?s=150">
      <br>
      <strong>Tangchaoyue</strong>
      <br>
      <a href="https://github.com/Tangchaoyue">@Tangchaoyue</a>
      <p>感谢 @Tangchaoyue(lfr) 的陪伴, 支持以及给项目起的名字,给我极大的信心与鼓励,并且在我编码的时候耐心的照顾我的生活</p>
    </td>
    <td align="center" valign="top">
      <img width="125" height="125" src="https://github.com/halfrost.png?s=150">
      <br>
      <strong>halfrost</strong>
      <br>
      <a href="https://github.com/halfrost">@halfrost</a>
      <p>感谢 @halfrost 大佬的文章</p>
      <a href="https://github.com/halfrost/Halfrost-Field/blob/master/contents/Go/go_spatial_search.md">高效的多维空间点索引算法
        — Geohash 和 Google S2</a>
      <a href="https://github.com/halfrost/Halfrost-Field/blob/master/contents/Go/go_s2_regionCoverer.md">Google
        S2 是如何解决空间覆盖最优解问题的</a>
    </td>
    <td align="center" valign="top">
      <img width="125" height="125" src="https://profile.csdnimg.cn/4/E/A/3_qq_43777978?s=150">
      <br>
      <strong>跳舞D猴子</strong>
      <br>
      <a href="https://blog.csdn.net/qq_43777978">@跳舞D猴子</a>
      <p>感谢 @跳舞D猴子 的文章</p>
      <a
          href="https://blog.csdn.net/qq_43777978/article/details/116800460?ivk_sa=1024320u">Google的S2算法原理以及使用Java版本</a>
    </td>
    <td align="center" valign="top">
      <img width="125" height="125" src="https://github.com/lssaidong.png?s=150">
      <br>
      <strong>lss</strong>
      <br>
      <a href="https://github.com/lssaidong">@lssaidong</a>
      <p>感谢 @lssaidong 贡献的项目 Logo</p>
    </td>
  </tr>
  <tr>
    <td align="center" valign="top">
      <img width="125" height="125" src="https://github.com/renyiwei-xinyi.png?s=150">
      <br>
      <strong>renyiwei-xinyi</strong>
      <br>
      <a href="https://github.com/renyiwei-xinyi">@renyiwei-xinyi</a>
      <p>感谢 @renyiwei-xinyi 的捐赠以及支持</p>
    </td>
  </tr>
  </tbody>
</table>
