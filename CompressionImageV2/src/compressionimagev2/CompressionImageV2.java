/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionimagev2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author USUÁRIO PADRÃO
 */
public class CompressionImageV2 {

    private static Logger logger = Logger.getGlobal();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        logger.info("Quantidade de args: " + args.length);
        if (args != null && args.length == 3) {
            logger.info("Quantidade de args: " + args.length);
            File origem = new File(args[0]);
            File destino = new File(args[1]);
            if (!origem.exists()) {
                logger.severe(String.format("Path da origem nÃ£o encontrado: %s", origem.getAbsolutePath()));
                return;
            }
            if (!destino.exists()) {
                logger.severe(String.format("Path do destino nÃ£o encontrado: %s", destino.getAbsolutePath()));
                if (destino.mkdirs()) {
                    logger.info(String.format("Path do destino criado: %s", destino.getAbsolutePath()));
                } else {
                    logger.severe(String.format("Path do destino nÃ£o foi criado: %s", destino.getAbsolutePath()));
                }
            }

            float qualidade = Float.parseFloat(args[2]);

            if (origem.isDirectory() && destino.isDirectory()) {
                File[] listFiles = origem.listFiles();
                logger.info(String.format("Foram encntrados %s arquivos para conversÃ£o.", listFiles.length));
                int errorCount = 0;
                for (File file : listFiles) {
                    if (comprimirImagem(file, destino, qualidade)) {
                        logger.info(new StringBuilder().append(file.getName()).append(" comprimida com sucesso!").toString());

                    } else {
                        errorCount++;
                        logger.info(new StringBuilder().append("Erro ao tentar comprimir a imagem ").append(file.getName()).toString());

                    }

                    if (errorCount > 2) {
                        logger.severe("Processamento interrompido por erros.");
                        break;
                    }
                }
            } else {
                logger.severe("ParÃ¢metros invÃ¡lidos. Origem e destino devem ser diretÃ³rios.");
            }
        } else {
            logger.severe("ParÃ¢metros invÃ¡lidos. Deve ser informado o diretÃ³rio de origem, diretÃ³rio de destino e a qualidade(0.0 até 0.99),quanto menor esse parametros mais será comprimido.");
        }

    }

    private static boolean comprimirImagem(File file, File destino, Float qualidade) {

        try {
            //File input = new File("digital_image_processing.jpg");
            BufferedImage image = ImageIO.read(file);

            File compressedImageFile = new File(destino, file.getName());

            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(qualidade);

            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Erro ao converter arquivo: %s", file.getName()), e.getMessage());
            return false;
        }

    }
}
