package com.core.automation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Arquivos {

    public static synchronized void moverArquivo(File caminhoDestino) throws IOException, InterruptedException {
        String caminhoOrigem = System.getProperty("user.dir") + "/target/arq/";
        if (!caminhoDestino.exists()) {
            caminhoDestino.mkdirs();
        }
        File[] files = new File(caminhoOrigem).listFiles();
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

        final Path caminho = Paths.get(caminhoOrigem + files[0].getName());
        final Path caminhoMovido = Paths.get(caminhoDestino + "/" + files[0].getName());
//        final Path caminho = Paths.get(caminhoOrigem + files[files.length - 1].getName());
//        final Path caminhoMovido = Paths.get(caminhoDestino + "/" + files[files.length - 1].getName());
        try {
            Files.move(caminho, caminhoMovido, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void descompactarArquivoZip(String caminho) {
        final int BUFFER = 2048;
        String name = null;
        File fileCaminho = new File(caminho);
        File[] files = fileCaminho.listFiles();
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

        Path zipFile = Paths.get(caminho + "/" + files[files.length - 1].getName());
        Path pastaDestino = Paths.get(caminho + "/" + fileCaminho.getName().split("\\.")[0] + "/");
        try {

            ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile));

            ZipEntry zipElement = zis.getNextEntry();

            while (zipElement != null) {

                Path newFilePath = pastaDestino.resolve(zipElement.getName());
                if (zipElement.isDirectory()) {
                    Files.createDirectories(newFilePath);
                } else {
                    if (!Files.exists(newFilePath.getParent())) {
                        Files.createDirectories(newFilePath.getParent());
                    }
                    try {
                        OutputStream bos = Files.newOutputStream(pastaDestino.resolve(newFilePath));

                        byte[] buffer = new byte[Math.toIntExact(zipElement.getSize())];

                        int location;

                        while ((location = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, location);
                        }
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                zipElement = zis.getNextEntry();
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<Object> getDadosColunaExcel(String caminhoArquivo, String nomeDaColuna) throws IOException {
        File arq = new File(caminhoArquivo);
        File[] files = arq.listFiles();
        List<Object> dados = new ArrayList();
        FileInputStream file = new FileInputStream(files[0]);
        try {
            Workbook wor = new XSSFWorkbook(file);
            Sheet sheet = wor.getSheetAt(0);
            Row cabecalho = sheet.getRow(0);

            // Encontrando a coluna no excel
            Iterator<Cell> colunaCabecalho = cabecalho.cellIterator();
            AtomicInteger indexColunaEscolhida = new AtomicInteger();
            colunaCabecalho.forEachRemaining(c -> {
                if (c.getStringCellValue().equals(nomeDaColuna)) {
                    indexColunaEscolhida.set(c.getColumnIndex());
                }
            });
            Iterator<Row> linha = sheet.rowIterator();
            linha.forEachRemaining(cells -> {
                switch (cells.getCell(indexColunaEscolhida.get()).getCellType()) {
                    case STRING:
                        dados.add(cells.getCell(indexColunaEscolhida.get()).getStringCellValue());
                        break;
                    case NUMERIC:
                        dados.add(NumberToTextConverter.toText(cells.getCell(indexColunaEscolhida.get()).getNumericCellValue()));
                        break;
                    case FORMULA:
                        try {
                            dados.add(cells.getCell(indexColunaEscolhida.get()).getStringCellValue());
                        } catch (Exception var13) {
                            dados.add(cells.getCell(indexColunaEscolhida.get()).getNumericCellValue());
                        }
                        break;
                    default:
                        dados.add("");
                }
            });
        } finally {
            file.close();
        }
        return dados;
    }

    public static ArrayList<Object> getDadosExcel(String caminhoArquivo, int linhaCabecalho) throws IOException {
        File arq = new File(caminhoArquivo);
        File[] files = arq.listFiles();
        ArrayList<Object> dados = new ArrayList<Object>();
        FileInputStream file = new FileInputStream(files[0]);
        Workbook wor = new XSSFWorkbook(file);
        Sheet sheet = wor.getSheetAt(0);
        Iterator<Row> linhaIterator = sheet.rowIterator();
        // conLinha retira o cabeçalho do arquivo escolhido
        int contLinha = -linhaCabecalho;
        Row linha = null;
        while (linhaIterator.hasNext()) {
            linha = linhaIterator.next();
            if (contLinha >= 0) {
                if (linha.getRowNum() != 0) {
                    for (int i = 0; i < linha.getLastCellNum(); i++) {
                        if (linha.getCell(i) == null) {
                            dados.add("");
                            continue;
                        }
                        switch (linha.getCell(i).getCellType()) {
                            case STRING:
                                dados.add(linha.getCell(i).getStringCellValue());
                                break;
                            case NUMERIC:
                                dados.add(NumberToTextConverter.toText(linha.getCell(i).getNumericCellValue()));
                                break;
                            case FORMULA:
                                try {
                                    dados.add(linha.getCell(i).getStringCellValue());
                                } catch (Exception e) {
                                    dados.add(linha.getCell(i).getNumericCellValue());
                                }
                                break;
                            case BLANK:
                                dados.add("");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            contLinha++;
        }
        file.close();
        return dados;
    }

    public static void remover(File arq) {
        File arqZip = new File(arq.getParentFile() + "/" + arq.getName() + ".zip");
        if (arqZip.exists()) {
            arqZip.delete();
        }
        if (arq.isDirectory()) {
            File[] elementos = arq.listFiles();
            for (int i = 0; i < elementos.length; i++) {
                remover(elementos[i]);
            }
        }
        arq.delete();
    }

    public static void remover(File arq, String nome, String extensao) {
        if (arq.isDirectory()) {
            File[] elementos = arq.listFiles();
            for (int i = 0; i < elementos.length; i++) {
                remover(elementos[i], nome, extensao);
            }
        }
        if (arq.getName().startsWith(nome) && arq.getName().endsWith(extensao)) {
            arq.delete();
        }
    }


    public static int quantidadeArquivosNaPasta(String caminho, String nomePasta) {
        File arquivos = new File(caminho + nomePasta);
        File[] files = null;
        if (arquivos.isDirectory()) {
            files = arquivos.listFiles();
        }
        return files.length;
    }

    public static void verificarTamanhoArquivo(String caminho) {
        File pasta = new File(caminho);
        FileFilter somenteArquivos = new FileFilter() {

            public boolean accept(File arquivo) {
                return arquivo.isFile();
            }
        };
        for (File arquivo : pasta.listFiles(somenteArquivos)) {
            assertTrue("Erro!!! Tamanho do arquivo \"" + arquivo.getName() + "\" está com valor 0.",
                    arquivo.length() > 0);
        }
    }
}
