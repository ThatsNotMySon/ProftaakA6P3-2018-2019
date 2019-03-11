package Data.tilemap;

import java.awt.image.BufferedImage;

public class Tile {

        private int tileID;
        private BufferedImage image;

        public Tile(int tileID, BufferedImage image){
            this.image = image;
            this.tileID = tileID;
        }

        public int getTileID(){
            return this.tileID;
        }

        public BufferedImage getImage(){
            return this.image;
        }
    }

