# 「Minecraft 1.12 圖片轉地圖工具」by FlashTeens
(Snapshot 0.1, 最近更新: 2018-01-15)
* **Note: This project currently supports "*Traditional Chinese*" only. English version will be available soon. Stay tuned! :)**
* 本工具僅適用於 Windows 作業系統，並且請確定您的電腦已安裝 Java 1.8 版本。
* 由於在 Minecraft 中使用「地圖」一詞可指「地圖物品」或「世界存檔」等涵義，容易造成混淆，
  因此本說明統一以「地圖物品」或「地圖檔」指稱地圖物品 (map item)，
  而「世界存檔」則用以指稱地圖存檔 (world save)。

## 操作說明
1. 將本資料夾解壓縮到您所想要的路徑。
2. 將想要轉換的圖片檔或地圖檔 (也可以是整個資料夾)，複製(或移動)到和執行檔同一資料夾內。
3. 將執行檔資料夾中所需要轉換的檔案 (可多選) 拖移到「FTMCMapImgConv.bat」中即可執行。
   - 若檔案名稱為「map_XXX.dat」(XXX需替換成數字) 將視為地圖檔，並會被轉換成 PNG 圖檔。
     * 輸出圖片檔的路徑: FTMCMapImgConv\map2img\
   - 若檔案是 PNG, JPEG 或 GIF(靜態) 圖檔，該檔案將被轉換成一個或多個地圖檔 (.dat)。
     * 若圖片大小為 128x128，將被轉換成單一地圖檔，檔名與圖片一致。
     * 若圖片的長度或寬度大於 128，但兩者皆為 128 的倍數，則該圖片轉檔後將分割成多個地圖檔。
       - 此種情況下會出現使用者輸入的畫面，需輸入起始編號 N‧。編號輸入完成後，程式將會依序產生 map_N.dat、map_(N+1).dat、map_(N+2).dat ... 等檔案，地圖檔的數量視圖片檔大小而決定。
       - 例1: 一張 256x384 的圖片，將被分割成 6 張地圖檔。編號順序的示意圖如下:<br>
             1 4<br>
             2 5<br>
             3 6
       - 例2: 一張 384x256 的圖片，將被分割成 6 張地圖檔。編號順序的示意圖如下:<br>
             1 3 5<br>
             2 4 6
       - 以上兩例中，若您在起始編號 N 的輸入欄位填入 101，產生的地圖檔名將會從 101 往上累加，即依照上列 1, 2, 3, 4, 5, 6 的順序，分別輸出 map_101.dat, map_102.dat, ..., map_106.dat 這些地圖檔。
       - 注意: 由於 Minecraft 地圖物品格式的限制，以上地圖物品的編號不能大於 32767。
     * 輸出地圖檔的路徑: FTMCMapImgConv\img2map\
   - 若是拖移資料夾到「FTMCMapImgConv.bat」中執行，則會分別判斷資料夾內 (包括所有子資料夾) 每個檔案的類型，再分別進行轉換。
   - 如果該檔案不符合以上所提及的檔案類型，則將自動被忽略，不進行轉換。

## 如何將轉檔後的地圖檔放進 Minecraft 中使用
1. 依照以上「操作說明」來產生地圖檔，將所有產生出來的 map_XXX.dat 地圖檔複製到「%appdata%\.minecraft\saves\您的世界存檔目錄\data\」路徑底下。
2. 從 Minecraft (需為 1.12 以上版本) 中開啟您的世界存檔，並輸入以下指令:<br>
   /give @p filled_map 1 XXX<br>
   (以上XXX需替換成數字)<br>
   即可得到對應的地圖物品。

## 如何取出某個 Minecraft 世界存檔中的地圖物品，並轉換成 PNG 圖檔
1. 從 Minecraft (1.8 以上版本即可) 中開啟世界存檔，並在創造模式中，
   在所想要轉換的地圖物品上按下滑鼠中鍵(滾輪鈕)，
   取得該地圖物品的編號 (ex.「地圖 #XXX」)。
2. 從檔案總管中開啟「%appdata%\.minecraft\saves\您的世界存檔目錄\data\」路徑，
   並將其中的「map_XXX.dat」檔案複製到本工具的執行檔資料夾中。(XXX為對應的地圖檔編號)
3. 將複製後的「map_XXX.dat」拖移到執行檔「FTMCMapImgConv.bat」中，即可得到轉換後的 PNG 圖片。

## 如何在 Eclipse 中使用本專案的 Java 原始碼
* 將 src 資料夾中的所有程式碼複製到您的 Eclipse 專案中即可，無需任何外部的 library dependencies。
* 請確保 JRE 的執行環境為 Java 1.8 版本。
* 匯出 JAR 執行檔時，請將 Launch configuration 的類別設定為 MapImageConverterMain。

## 其他注意事項
* 如需單純測試本工具的轉檔功能，您可試著將壓縮檔內的「example_img2map」或「example_map2img」資料夾拖移到「FTMCMapImgConv.bat」，
  並觀察程式會產生哪些檔案。

* 目前本程式的「圖片轉地圖」功能尚未支援「遞色 (Dither)」功能，將於未來版本中實現，敬請期待！
