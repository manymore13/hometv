import base64

def encode_file_to_base64(input_file, output_file):
    """将文件编码为 Base64 并保存到文本文件中"""
    try:
        with open(input_file, "rb") as file:
            binary_data = file.read()
        # Base64 编码
        encoded_data = base64.b64encode(binary_data).decode("utf-8")
        
        # 将编码结果保存到输出文件
        with open(output_file, "w", encoding="utf-8") as encoded_file:
            encoded_file.write(encoded_data)
        print(f"文件已成功编码为 Base64，并保存到 {output_file}")
    except Exception as e:
        print(f"编码失败: {e}")

def decode_base64_to_file(input_file, output_file):
    """将 Base64 文本解码为原始文件"""
    try:
        with open(input_file, "r", encoding="utf-8") as encoded_file:
            encoded_data = encoded_file.read()
        # Base64 解码
        binary_data = base64.b64decode(encoded_data)
        
        # 将解码结果保存到输出文件
        with open(output_file, "wb") as file:
            file.write(binary_data)
        print(f"Base64 文本已成功解码为文件，并保存到 {output_file}")
    except Exception as e:
        print(f"解码失败: {e}")


# encode_file_to_base64("hometv1.jks", "encoded_file.txt")

decode_base64_to_file("encoded_file.txt", "hometv.jks")


