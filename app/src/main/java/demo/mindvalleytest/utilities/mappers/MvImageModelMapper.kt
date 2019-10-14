package demo.mindvalleytest.utilities.mappers;


import demo.mindvalleytest.data.models.DTO.MVImages;
import demo.mindvalleytest.data.models.local.MvImagesLocal;

object MvImageModelMapper {
    fun mapRemoteVideoToLocal(mvImages: MVImages): MvImagesLocal {
        return MvImagesLocal(
            mvImages.id,
            mvImages.color,
            mvImages.createdAt,
            mvImages.height,
            mvImages.width,
            mvImages.likes,
            mvImages.urls,
            mvImages.user
        )
    }
}