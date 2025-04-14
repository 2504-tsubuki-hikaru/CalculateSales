package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateSales {

	// 支店定義ファイル名
	private static final String FILE_NAME_BRANCH_LST = "branch.lst";

	// 支店別集計ファイル名
	private static final String FILE_NAME_BRANCH_OUT = "branch.out";

	// エラーメッセージ
	private static final String UNKNOWN_ERROR = "予期せぬエラーが発生しました";
	private static final String FILE_NOT_EXIST = "支店定義ファイルが存在しません";
	private static final String FILE_INVALID_FORMAT = "支店定義ファイルのフォーマットが不正です";

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {
		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();

		////3－1コマンドライン引数が1つ設定されていなかった場合はエラーを出力する。
		if (args.length != 1) {
		    System.out.println("予期せぬエラーが発生しました");
		    return;
		}

		// 支店定義ファイル読み込み処理
		if(!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {
			return;
		}

		// ※ここから集計処理を作成してください。(処理内容2-1、2-2)完了済
		File[] files = new File(args[0]).listFiles();

		List<File> rcdFiles = new ArrayList<>();

		for(int i = 0; i < files.length ; i++) {
			//「^…先頭」「[0-9]の間の数字は通す」「{8}」桁数を指定　「.…任意の1文字」「rcd…rcdという文字列」「$…末尾」
			if(ファイルの情報.isFile() && files[i].getName().matches("^[0-9]{8}[.]rcd$")) {

				rcdFiles.add(files[i]);
			}
		}

		Collections.sort(rcdFiles);

		// 繰り返し
		for(int i = 0; i < rcdFiles.size() - 1; i++) {

			//エラー処理2－1
			//⽐較する2つのファイル名の先頭から数字の8文字を切り出し、int型に変換する処理。
			int former = Integer.parseInt(rcdFiles.get(i).getName().substring(0, 8));
			int latter = Integer.parseInt(rcdFiles.get(i+1).getName().substring(0, 8));

			//2つのファイル名の数字を比較して、差が1ではないか確認する処理
			if((latter - former) != 1) {
				System.out.println("ファイル名が連番になっていません");
				return;
			}
		}
		// 繰り返し終わり

		for(int i = 0; i < rcdFiles.size(); i++) {
			BufferedReader br = null;

			//読込
			try {
				File file = new File(args[0],rcdFiles.get(i).getName());
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line;

				//ファイルの中身(読み込んだもの)を格納するための変数：fileContents
				List<String> fileContents = new ArrayList<>();

				//売上ファイルを１行ずつ読み込む　１行目…支店コード　２行目…売上金額
				while((line = br.readLine()) != null) {

					//読んだ行(１行目だったり２行目)を、リスト「list」にaddしている
					fileContents.add(line);
				}

				//売上ファイルが2行か確かめる処理
				if(fileContents.size() != 2) {
					System.out.println("<該当ファイル名>のフォーマット不正です");
					return;
				}

				if (!branchNames.containsKey(branchNames)) {
				    //⽀店情報を保持しているMapに売上ファイルの⽀店コードが存在しなかった場合は、
				    //エラーメッセージをコンソールに表⽰します。
					System.out.println("<該当ファイル名>の支店コードが不正です");
					return;
				}

				//数値化確かめる処理
				if(!売上⾦額.matches.("\"^[0-9]{8}[.]rcd$\"")) {
				    //売上⾦額が数字ではなかった場合は、
				    //エラーメッセージをコンソールに表⽰します。
				}

				//読込後、型変換
				long fileSale = Long.parseLong(fileContents.get(1));

				//型変換後、売上金額を加算
				Long saleAmount = branchSales.get(fileContents.get(0)) + fileSale;
				//エラー処理 2
				//売上金額が11桁以上の場合はエラーメッセージを出力する。
				if(saleAmount >= 10000000000L){
					System.out.println("合計⾦額が10桁を超えました");
					return;
				}

				//branchSalesマップに格納
				branchSales.put(fileContents.get(0), saleAmount);

			} catch(IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return;
			} finally {
				// ファイルを開いている場合
				if (br != null) {
					try {
						// ファイルを閉じる
						br.close();
					} catch (IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return;
					}
				}
			}
		}

		// 支店別集計ファイル書き込み処理
		if(!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {
			return;
		}
	}


	/**
	 * 支店定義ファイル読み込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 読み込み可否
	 */
	private static boolean readFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		BufferedReader br = null;

		//ファイル読込処理
		try {
			File file = new File(path, fileName);
			//3－1の処理の場所は支店定義ファイルの読込をしている所だからこの場所になる（FileReader.BufferedReader）
			//「new File(path, fileName)(＝branch.lst)」が存在する場合はtrue、存在しない場合はfalseを返す。（！は否定）
			if(!file.exists()) {
			    //支店定義ファイルが存在しない場合は(false)コンソールにエラーメッセージを表示する。
				System.out.println("支店定義ファイルが存在しません");
				//処理を終了させる処理
				return false;
			}
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				//支店定義ファイルの条件が合っていない場合、エラーメッセージをコンソールに表示する。

				// ※ここの読み込み処理を変更してください。(処理内容1-2)完了済み
				String[] items  = line.split(",");

				//⽀店定義ファイルの仕様が満たされていないか確かめる処理
				//OR演算子（どちらかが真なら真を返す）
				if((items.length != 2) || (!items[0].matches("^[0-9]{3}$"))){
				     //ファイルの使用が満たされていない場合はエラーメッセージを出力する。
					System.out.println("支店定義ファイルのフォーマットが不正です");
				}

				branchNames.put(items[0], items[1] );
				branchSales.put(items[0],  0L);
				}
			} catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if (br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch (IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}


	private static Object line() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	/**
	 * 支店別集計ファイル書き込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 書き込み可否
	 */

	private static boolean writeFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		// ※ここに書き込み処理を作成してください。(処理内容3-1)
		BufferedWriter bw = null;

		try {
			File file = new File(path, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			// Mapから全てのKeyを取得処理
			for (String key : branchSales.keySet()) {
				//keyという変数には、Mapから取得したキーが代入されています。
				//拡張for⽂で繰り返されているので、1つ⽬のキーが取得できたら、
				//2つ⽬の取得...といったように、次々とkeyという変数に上書きされていきます

				// 所得したkeyを支店別集計ファイル(branch.out)に書込む処理
				bw.write(key + "," + branchNames.get(key) + "," + branchSales.get(key));
				bw.newLine();
			}

		} catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
		// ファイルを開いている場合
			if (bw != null) {
				try {
				// ファイルを閉じる
					bw.close();
				} catch (IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}
}