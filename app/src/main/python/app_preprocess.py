import librosa

def preprocess(file_path):

    maxlen = 22050


    signal, sample_rate = librosa.load(file_path)

    if len(signal) >= maxlen:
        signal = signal[:maxlen]

    MFCCs = librosa.feature.mfcc(signal, sample_rate, n_mfcc=13, n_fft=2048, hop_length=512)
    return bytes(MFCCs)